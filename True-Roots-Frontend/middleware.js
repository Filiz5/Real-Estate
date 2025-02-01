import { NextResponse } from 'next/server';
import { auth } from '@/auth';

export function middleware(request) {
  const { pathname } = new URL(request.url);

  // Apply middleware only to specific routes
  if (pathname.startsWith('/dashboard') || pathname.startsWith('/admin-dashboard') || pathname === '/login') {
    return auth(request);
  }

  // Allow all other requests to pass through
  return NextResponse.next();
}

// Specify the routes the middleware should apply to
export const config = {
  matcher: ['/((?!.+\\.[\\w]+$|_next).*)', '/', '/(api|tprc)(.*)']
};