'use client';

import { MainFooter } from '@/components/common/footer/main-footer';
import { MainMenubar } from '@/components/common/header/main-menubar';
import styles from '@/styles/layouts/public-layout.module.scss';
import { useState, Suspense, useEffect } from 'react';
import UserSidebar from '@/components/common/user_sidebar/UserSidebar';

export default function LayoutComponent({ session, children }) {
  const [isUserSidebarOpen, setUserIsSidebarOpen] = useState(false);
  const [newSession, setNewSession] = useState(null);

  useEffect(() => {
    if (session) {
      setNewSession(session);
    }
  }, [session]);

  const toggleUserSidebar = (e) => {
    e.preventDefault();
    setUserIsSidebarOpen((prev) => !prev);
  };

  return (
    <div className={styles.container}>
      <UserSidebar
        isOpen={isUserSidebarOpen}
        toggleUserSidebar={toggleUserSidebar}
      />
      <div className={styles.main}>
        {session ? (
          <MainMenubar
            session={newSession}
            toggleUserSidebar={toggleUserSidebar}
          />
        ) : (
          <MainMenubar />
        )}
        <div className={styles.innerContainer}>{children}</div>
        <div className={styles.footer}>
          <MainFooter />
        </div>
      </div>
    </div>
  );
}
