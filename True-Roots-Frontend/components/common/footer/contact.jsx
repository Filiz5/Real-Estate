import { brand } from '@/constants/brand';
import styles from '@/styles/components/common/footer/contact.module.scss';

export const Contact = () => {
  return (
    <div className={styles.contact}>
      <p className={styles.title}>Contact</p>
      {/* Address Link */}
      <p className={styles.address}>
        <a
          href={`https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(
            brand.address
          )}`}
          target="_blank"
          rel="noopener noreferrer"
          className={styles.link}
        >
          {brand.address},<br />
          {brand.zipCode} {brand.counter}.
        </a>
      </p>
      {/* Phone Link */}
      <p className={styles.phone}>
        <a href={`tel:${brand.phone}`} className={styles.link}>
          {brand.phone}
        </a>
      </p>
      {/* Email Link */}
      <p className={styles.email}>
        <a href={`mailto:${brand.email}`} className={styles.link}>
          {brand.email}
        </a>
      </p>
    </div>
  );
};
