'use client';
import Link from 'next/link';
import { MdOutlineCallMade } from 'react-icons/md';
import { FiPhoneCall } from 'react-icons/fi';
import { brand } from '@/constants/brand';
import styles from '@/styles/components/common/contact-help-section/contact-help.module.scss';

export const ContactHelp = () => {
  const phoneNumber = '+1234567890';
  return (
    <div className={styles.container}>
      <div className={styles.text}>
        <p className={styles.title}>Need help? Talk to our expert.</p>
        <p className={styles.body}>
          Talk to our experts or Browse through more properties.
        </p>
      </div>
      <div className={styles.buttons}>
        <a className={styles.firstButton} href={`mailto:${brand.email}`}>
          Email Us <MdOutlineCallMade />
        </a>
        <button
          className={styles.secondButton}
          onClick={() => {
            window.location.href = `tel:${brand.phone}`;
          }}
        >
          <FiPhoneCall /> Contact Us
        </button>
      </div>
    </div>
  );
};
