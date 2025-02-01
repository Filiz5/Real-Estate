import Link from 'next/link';
import menuItems from '@/data/main-menu.json';
import styles from '@/styles/components/common/footer/quik-links.module.scss';

export const QuikLinks = () => {
  return (
    <div className={styles.quikLinks}>
      <p>Quick Links</p>

      <ul className={styles.menuList}>
        {menuItems.map((item) => (
          <li key={item.link} className={styles.menuItem}>
            <Link href={item.link} className={styles.menuLink}>
              {item.title}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
};

