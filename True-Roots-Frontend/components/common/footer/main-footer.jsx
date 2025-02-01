import React from 'react'
import { Slogan } from "@/components/common/footer/slogan";
import { QuikLinks } from '@/components/common/footer/quick-links'
import { Contact } from '@/components/common/footer/contact';
import { SiAppstore } from 'react-icons/si';
import { PiGooglePlayLogo } from 'react-icons/pi';
import { BsTwitterX } from 'react-icons/bs';
import { FiFacebook } from 'react-icons/fi';
import { FaInstagram } from 'react-icons/fa';
import { SlSocialLinkedin } from 'react-icons/sl';
import styles from '@/styles/components/common/footer/main-footer.module.scss';
export const MainFooter = () => {
  return (
    <footer className={styles.footer}>
      <div className={styles.container}>
        <div className={styles.slogan}>
          <Slogan />
        </div>
        <div className={styles.quikLinks}>
          <QuikLinks />
        </div>

        <div className={styles.contact}>
          <Contact />
        </div>
      </div>
      <div className={styles.bottomSection}>
        <div className={styles.copyRight}>
          &copy; 2025 TrueRoots LLC. All rights reserved.
        </div>
      </div>
    </footer>
  );
};
