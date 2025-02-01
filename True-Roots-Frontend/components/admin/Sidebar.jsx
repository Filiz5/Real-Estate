'use client';
import SidebarLinks from './SidebarLinks';
import AdminLogoutButton from './AdminLogoutButton';
import styles from '@/styles/components/admin/sidebar.module.scss';
import { BiHomeSmile } from 'react-icons/bi';

export default function Sidebar({ isOpen, onToggleSidebar }) {
  return (
    <aside
      className={`${styles.sidebar} ${isOpen ? styles.open : styles.closed}`}
    >
      <div className={styles.close_button} onClick={onToggleSidebar}>
        x
      </div>
      <div className={styles.logo}>
        <BiHomeSmile />
        Warmy Homes
      </div>
      <nav className={styles.links}>
        <div className={styles.linksContainer}>
          <SidebarLinks onToggleSidebar={onToggleSidebar} />
        </div>
        <div className={styles.logoutContainer}>
          <AdminLogoutButton />
        </div>
      </nav>
    </aside>
  );
}
