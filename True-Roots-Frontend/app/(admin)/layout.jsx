'use client';

import { Suspense, useState } from 'react';
import Sidebar from '@/components/admin/Sidebar';
import AdminHeader from '@/components/admin/AdminHeader';
import styles from '@/styles/components/admin/admin-layout.module.scss';
import Loading from './admin-dashboard/loading';

export default function AdminLayout({ children }) {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const toggleSidebar = (e) => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  return (
    <div className={styles.layout}>
      <Sidebar isOpen={isSidebarOpen} onToggleSidebar={toggleSidebar} />
      <div className={styles.main}>
        <AdminHeader onToggleSidebar={toggleSidebar} />
        <Suspense fallback={<Loading />}>
        <div className={styles.content}>{children}</div>
        </Suspense>
      </div>
    </div>
  );
}
