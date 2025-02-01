import { Contact } from '@/components/common/contact/contact';
import UserPageHeader from '@/components/common/UserPageHeader';
import { wait } from '@/utils/wait';

const ContactPage = async () => {
  await wait(2000); // Simulate loading time
  return (
    <>
      <UserPageHeader text="Contact" />
      <Contact />
    </>
  );
};

export default ContactPage ;
