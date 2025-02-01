'use client';
import styles from '@/styles/components/admin/admin-header.module.scss';
import { breadCrumb } from '@/data/bread-crumb';
import { useEffect, useState } from 'react';
import { usePathname, useRouter } from 'next/navigation';
import { wait } from '@/utils/wait';

export default function AdminHeader({ onToggleSidebar }) {
    const pathname = usePathname();
    const router = useRouter();
    
    const page = breadCrumb.find((item) =>
      item.urlRegex.test(pathname)
    );

    useEffect(() => {
    }, [page, pathname]);

    // if(pathname.startsWith('/login')) {
    //     router.push('/admin-dashboard');
    //     wait(2000);
    //     return true;
    // }

    

  return (
    <header className={styles.header}>
      <button className={styles.hamburger} onClick={onToggleSidebar}>
        &#9776; {/* Hamburger menu */}
      </button>
      <div className={styles.breadcrumb}>Home / {page?.title||'No Title Avaible'}</div>
    </header>
  );
}
