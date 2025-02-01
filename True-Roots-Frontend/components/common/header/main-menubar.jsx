'use client';

import React, { useEffect, useState } from 'react';
import { Logo } from '@/components/common/logo';
import { MainMenu } from '@/components/common/header/main-menu';
import { LoginButton } from '@/components/common/header/login-button';
import { AddProperty } from '@/components/common/header/add-property-button';
import { TiThMenu } from 'react-icons/ti';
import { IoClose, IoPersonCircle } from 'react-icons/io5'; // Close icon
import styles from '@/styles/components/common/header/main-menubar.module.scss';
import LogoutButton from '../LogoutButton';
import ProfileMenu from './ProfileMenu';
import Link from 'next/link';

export const MainMenubar = ({ session, toggleUserSidebar }) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [screenSize, setScreenSize] = useState({
    width: 0,
    height: 0,
  });

  useEffect(() => {
    if (typeof window !== 'undefined') {
      setScreenSize({
        width: window.innerWidth,
        height: window.innerHeight,
      });

      const handleResize = () => {
        setScreenSize({
          width: window.innerWidth,
          height: window.innerHeight,
        });
      };

      window.addEventListener('resize', handleResize);

      return () => {
        window.removeEventListener('resize', handleResize);
      };
    }
  }, []);

  useEffect(() => {}, [session]);
  const name = session?.user?.object?.name;
  const lastName = session?.user?.object?.lastName;
  const accessToken = session?.accessToken || null;
  const role = session?.user?.object?.roles[0];

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  // Toggles the profile popup
  return (
    <header className={styles.mainMenubar}>
      <div className={styles.logo}>
        <Logo filename={'logo1'} />
        {accessToken
          ? screenSize.width < 768 && (
              <div
                title="User Sidebar Menu"
                className={styles.hamburger}
                onClick={toggleUserSidebar}
              >
                &#9776; {/* Hamburger menu */}
              </div>
            )
          : null}
      </div>

      {/* Hamburger Menu for small screens */}
      <button className={styles.menuToggle} onClick={toggleSidebar}>
        {sidebarOpen ? <IoClose size={24} /> : <TiThMenu size={24} />}
      </button>

      {/* Sidebar for small screens */}
      <nav className={`${styles.navMenu} ${sidebarOpen ? styles.open : ''}`}>
        <MainMenu />
        {!accessToken && <LoginButton />}
        {role === 'ADMIN' && <Link href="/admin-dashboard">Admin Home</Link>}
        <AddProperty session={session} />
      </nav>

      {/* Main Menu for large screens */}
      <div className={styles.mainMenuDesktop}>
        <MainMenu />
        {role === 'ADMIN' && <div className={styles.admin_home}><Link href="/admin-dashboard">Admin Home</Link></div>}
      </div>

      {/* Right-side buttons for large screens */}
      <div className={styles.rightComponents}>
        {accessToken ? (
          <ProfileMenu name={name} lastName={lastName} />
        ) : (
          <LoginButton />
        )}
        <AddProperty />
      </div>
    </header>
  );
};
