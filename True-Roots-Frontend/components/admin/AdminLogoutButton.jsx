import { signOut } from 'next-auth/react';
import styles from '@/styles/components/admin/admin-logout-button.module.scss'; 
import { IoIosLogOut } from 'react-icons/io';

const LogoutButton = () => {
    const handleSignOut = () => {
        signOut({ callbackUrl: '/' });
    };
    return (
      <button type="button" title="Logout" className={styles.button} onClick={handleSignOut}>
        <span>
          <IoIosLogOut size={30} />
        </span>
        <span>Logout</span>
      </button>
    );
}

export default LogoutButton;