import UserPageHeader from '@/components/common/UserPageHeader';
import { PrivacyPolicy } from '@/components/privacyPolicy/PrivacyPolicy';

const Page = () => {
  return (
    <>
      <UserPageHeader text="PRIVACY POLICY" />
      <PrivacyPolicy />
    </>
  );
};

export default Page;
