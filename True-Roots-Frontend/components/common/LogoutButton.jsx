import { signOut } from 'next-auth/react';
import styles from '@/styles/components/common/logout-button.module.scss'; 

const LogoutButton = () => {
    const handleSignOut = () => {
        signOut({ callbackUrl: '/' });
    };
    return (
      <div className={styles.logout} onClick={handleSignOut}>
        Logout
        <div className={styles.underline}></div>
      </div>
    );
}

export default LogoutButton;