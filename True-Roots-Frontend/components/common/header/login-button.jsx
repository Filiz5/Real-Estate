
import { GoPerson } from 'react-icons/go';
import styles from '@/styles/components/common/header/login-button.module.scss';
import Link from 'next/link';
import { IoMdPersonAdd } from 'react-icons/io';

export const LoginButton = () => {

  return (
    <div className={styles.container}>
      <Link href={'/login'}  className={styles.loginLink}>
        <GoPerson className={styles.goPerson} />
        <span>Login</span>
      </Link>
     <span>or</span>
      <Link href={'/login/register'} className={styles.loginLink}>
        <IoMdPersonAdd className={styles.goPerson} />
        <span> Register</span>
      </Link>
    </div>
  );
};
