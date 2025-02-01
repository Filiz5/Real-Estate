
import { BiHomeSmile } from 'react-icons/bi';
import UserSidebarLinks from './UserSidebarLinks';
import styles from '@/styles/components/common/user-sidebar/user-sidebar.module.scss';
import AdminLogoutButton from '@/components/admin/AdminLogoutButton';

export default function UserSidebar({ isOpen, toggleUserSidebar }) {
  return (
    <aside
      className={`${styles.sidebar} ${isOpen ? styles.open : styles.closed}`}
    >
      <div  className={styles.close_button} onClick={toggleUserSidebar}>x</div>
      <div className={styles.logo}>
        <BiHomeSmile className={styles.logo_image}/>
        <p>True Roots</p>
      </div>
      <nav className={styles.links}>
        <div className={styles.linksContainer}>
          <UserSidebarLinks />
        </div>
        <div className={styles.logoutContainer}>
          <AdminLogoutButton />
        </div>
      </nav>
    </aside>
  );
}
