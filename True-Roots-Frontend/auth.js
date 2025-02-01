import NextAuth from 'next-auth';
import Credentials from 'next-auth/providers/credentials';
import { login, loginWithGoogle } from './services/auth-service';
import { getIsTokenValid, getIsUserAuthorized } from '@/helpers/auth-helper';
import GoogleProvider from 'next-auth/providers/google';

const config = {
  providers: [
    Credentials({
      async authorize(credentials) {
        const res = await login(credentials);

        if (!res.ok) return null;
        const data = await res.json();
        const payload = {
          user: { ...data },
          accessToken: data.object.token
        };
        delete payload.user.token;
        return payload;
      }
    }),
    GoogleProvider({
      clientId: process.env.GOOGLE_CLIENT_ID,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET
    })
  ],
  callbacks: {
    async signIn(user, account, profile) {
      if (user?.account?.provider === 'google') {
        // Send Google user info to backend for validation
        const payload = {
          email: user.user.email,
          name: user.profile.given_name,
          lastName: user.profile.family_name,
          idToken: user.account.id_token || user.profile.given_name
        };
        const res = await loginWithGoogle(payload);
        const data = await res.json();

        if (res.ok && data) {
          user.user.backendData = {
            accessToken: data.object.token, // Backend token
            user: { ...data } // User details (email, roles, etc.)
          };

         return true;
        }
        return false;
      }
      return true;
    },
    // middleware in kapsama alanina giren sayfalara yapilan isteklerden hemen once calisir
    authorized({ auth, request }) {
      const { pathname, searchParams } = request.nextUrl;
      const redirectLink = searchParams.get('redirect');

      const userRole = auth?.user?.object?.roles[0];
      const roles = auth?.user?.object?.roles;

      const isLoggedIn = !!userRole;
      const isInLoginPage = pathname.startsWith('/login');
      const isInDashboardPages = pathname.startsWith('/dashboard');
      const isInAdminDashboardPages = pathname.startsWith('/admin-dashboard');
      const isTokenValid = getIsTokenValid(auth?.accessToken);

      if (isLoggedIn && isTokenValid) {
        if (isInDashboardPages || isInAdminDashboardPages) {
          const isUserAuthorized = getIsUserAuthorized(userRole, pathname);
          if (isUserAuthorized) return true;

          return Response.redirect(new URL('/unauthorized', request.nextUrl));
        } else if (isInLoginPage) {
          return Response.redirect(
            new URL(
              roles.includes('ADMIN') || roles.includes('MANAGER')
                ? '/admin-dashboard'
                : redirectLink || '/',
              request.nextUrl
            )
          );
        }
      } else if (isInDashboardPages || isInAdminDashboardPages) {
        return false;
      }

      return true;
    },
    // JWT datasina ihtiyac duyan her yerde
    async jwt({ token, user, account }) {
      if (account?.provider === 'google' && user.backendData) {
        token.accessToken = user.backendData.accessToken; // Backend token
        token.user = user.backendData.user; // Backend user details
        return token;
      }
      return {...user, ...token};
    },
    // Session datasina ihtiyac duyan her yerde
    async session({ session, token }) {
      const isTokenValid = getIsTokenValid(token.accessToken);
      if (!isTokenValid) return null;

      session.accessToken = token.accessToken;
      session.user = token.user;
      return session;
    }
  },

  pages: {
    signIn: '/login'
  },
  trustHost: ['localhost']
};

export const { handlers, auth, signIn, signOut } = NextAuth(config);
