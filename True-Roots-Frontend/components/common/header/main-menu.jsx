
import Link from 'next/link';
import menuItems from '@/data/main-menu.json';
import styles from '@/styles/components/common/header/main-menu.module.scss'; 

export const MainMenu = (props) => {
  return (
    <nav className={styles.mainMenu} {...props}>
    
      <ul className={styles.menuList}>
  
        {menuItems.slice(0, -1).map((item) => (
          <li key={item.link} className={styles.menuItem}>
            
            <Link href={item.link} className={styles.menuLink }>
              
              {item.title}
            </Link>
          </li>
        ))}
      </ul>
    </nav>
  );
};
