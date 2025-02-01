import { Logo } from '@/components/common/logo';
import { brand } from '@/constants/brand';
import { SiAppstore } from 'react-icons/si';
import { PiGooglePlayLogo } from 'react-icons/pi';
import { BsTwitterX } from 'react-icons/bs';
import { FiFacebook } from 'react-icons/fi';
import { FaInstagram } from 'react-icons/fa';
import { SlSocialLinkedin } from 'react-icons/sl';
import styles from '@/styles/components/common/footer/slogan.module.scss';

export const Slogan = () => {
  return (
    <div className={styles.slogan}>
      <div className={styles.logo}>
        <Logo filename={'logo-white2'} width={150} height={75} />
      </div>

      <p className={styles.p}>{brand.desscription}</p>

      <div className={styles.bottomSection}>
        <div className={styles.mediaButtons}>
          <a
            href="https://apps.apple.com/app"
            className={styles.mediaButton}
            aria-label="Apple"
          >
            <SiAppstore />
          </a>
          <a
            href="https://play.google.com/store/apps21"
            className={styles.mediaButton}
            aria-label="Play"
          >
            <PiGooglePlayLogo />
          </a>
          <a
            href="https://twitter.com"
            className={styles.mediaButton}
            aria-label="Twitter"
          >
            <BsTwitterX />
          </a>
          <a
            href="https://facebook.com"
            className={styles.mediaButton}
            aria-label="Facebook"
          >
            <FiFacebook />
          </a>
          <a
            href="https://instagram.com"
            className={styles.mediaButton}
            aria-label="Instagram"
          >
            <FaInstagram />
          </a>
          <a
            href="https://linkedin.com"
            className={styles.mediaButton}
            aria-label="LinkedIn"
          >
            <SlSocialLinkedin />
          </a>
        </div>
      </div>
    </div>
  );
};
