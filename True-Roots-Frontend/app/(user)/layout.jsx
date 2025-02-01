import { auth } from '@/auth';
import LayoutComponent from '@/components/dashboard/LayoutComponent';

export default async function PublicLayout({ children }) {
  const session = await auth();

  return (
    <>
      <LayoutComponent session={session}>{children}</LayoutComponent>
    </>
  );
}
