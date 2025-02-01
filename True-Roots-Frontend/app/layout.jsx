import { Roboto } from 'next/font/google';
import localFont from 'next/font/local';
import { brand } from '@/constants/brand';
import '@/styles/styles.scss';
import 'primeicons/primeicons.css';

export const metadata = {
  title: {
    template: `%s | ${brand.name}`,
    default: `${brand.name} - ${brand.title}`
  },
  description: brand.desscription,
  icons: {
    icon: '/favicon.ico'
  }
};

const roboto = Roboto({
  weight: ['100', '300', '400', '500', '700', '900'],
  style: ['italic', 'normal'],
  subsets: ['latin'],
  display: 'swap',
  variable: '--font-roboto'
});

const modulus = localFont({
  src: [
    {
      path: '../public/assets/fonts/Modulus.ttf',
      weight: '400',
      style: 'normal'
    },
    {
      path: '../public/assets/fonts/Modulus-Bold.otf',
      weight: '700',
      style: 'normal'
    },
    {
      path: '../public/assets/fonts/Modulus_Medium.ttf',
      weight: '500',
      style: 'normal'
    }
  ],
  variable: '--font-modulus'
});

export default function RootLayout({ children }) {
  return (
    <html lang="en" className={`${roboto.variable} ${modulus.variable}`}>
      <body>
        <main>{children}</main>
      </body>
    </html>
  );
}
