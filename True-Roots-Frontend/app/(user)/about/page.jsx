import UserPageHeader from '@/components/common/UserPageHeader';
import { AboutUs } from '@/components/about/about-us';
import {ContactHelp} from '@/components/common/contact-help-section/contact-help'

export const metadata = {
  title: 'About US',
  description:
    'Learn more about our organization. Our team, our mission, and our vision.'
};

const Page = () => {
  return (
    <>
      <UserPageHeader text="ABOUT US" />
      <AboutUs />
      <ContactHelp />
    </>
  );
};

export default Page;
