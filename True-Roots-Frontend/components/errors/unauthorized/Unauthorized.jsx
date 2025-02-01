'use client';
import Image from 'next/image';
import React from 'react';
import { signOut } from 'next-auth/react';
import styles from '@/styles/components/unauthorized/unauthorized.module.scss';
import Link from 'next/link';

const UnAuthorized = () => {
  const handleSignOut = () => {
    signOut({ callbackUrl: '/' });
  };

return (
    <div className={styles.container}>
        <div className={styles.inner_container}>
            <div className={styles.image_container}>
                <Image
                    src="/errors/404 1.png"
                    width={500}
                    height={500}
                    className={styles.image}
                    alt="Unauthorized"
                />
            </div>
            <div className={styles.text_container}>
                <h2>OOps! It looks like you&apos;re lost.</h2>
                <p>
                    It looks like you are already logged in but trying to access a page you do not have permission to view. Please log out and log in again or go to page, you have permission to view.
                </p>
                <div className={styles.buttons_container}>
                  <button className={styles.button} onClick={handleSignOut}>
                    Logout
                </button> 
                <Link href="/" className={styles.link}>Home Page</Link>
                </div>
                
            </div>
        </div>
    </div>
);
};

export default UnAuthorized;
