import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { userSidebarData } from '@/data/user-sidebar-data'
import styles from '@/styles/components/common/user-sidebar/user-sidebar-links.module.scss'

export default function UserSidebarLinks() {
    const pathname = usePathname();
  return userSidebarData.map((item, index) => (
    <Link
      key={index}
      href={item.pathname}
      className={`${styles.linkItem} ${
        pathname === item.pathname ? styles.activeLink : ''
      }`}
    >
      {item.title}
    </Link>
  ));
}
