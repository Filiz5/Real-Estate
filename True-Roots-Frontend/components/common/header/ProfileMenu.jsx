"use client";

import React, { useState } from 'react'
import { IoPersonCircle } from 'react-icons/io5';
import LogoutButton from '../LogoutButton';
import styles from '@/styles/components/common/header/profile-menu.module.scss';
import { userSidebarData } from '@/data/user-sidebar-data';
import { usePathname } from 'next/navigation';
import Link from 'next/link';
import { TbLockPassword } from 'react-icons/tb';


export default function ProfileMenu({ name, lastName }) {

     const [showProfile, setShowProfile] = useState(false);
     const pathname = usePathname();

    const toggleProfilePopup = () => {
      setShowProfile((prev) => !prev);
    };
  return (
    <div className={styles.container}>
      <div className={styles.profile_container}>
        <div className={styles.name}>
          <p>Hi, {name}</p>
        </div>
        <div>
          <IoPersonCircle
            size={55}
            className={styles.profileIcon}
            onClick={toggleProfilePopup}
            onMouseEnter={() => setShowProfile(true)}
            onMouseLeave={() => setShowProfile(false)}
          />
        </div>
      </div>
      {showProfile && (
        <div
          className={styles.profilePopup}
          onMouseEnter={() => setShowProfile(true)}
          onMouseLeave={() => setShowProfile(false)}
        >
          <div className={styles.links_container}>
            {userSidebarData.map((item, index) => (
              <Link
                key={index}
                href={item.pathname}
                className={`${styles.link} ${
                  pathname === item.pathname ? styles.activeLink : ''
                }`}
              >
                <span>{item.icon}</span><span>{item.title}</span>
                
                
                
              </Link>
            ))}
          </div>

          <LogoutButton />
        </div>
      )}
    </div>
  );
}
