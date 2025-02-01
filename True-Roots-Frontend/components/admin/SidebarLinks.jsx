"use client";

import { sidebarData } from "@/data/sidebar-data";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import styles from "@/styles/components/admin/sidebar-links.module.scss";

export default function SidebarLinks({onToggleSidebar}) {

    const pathname = usePathname();
    const router = useRouter();

    const handleClose = async (path) => {

        await onToggleSidebar();
        router.push(path);
    }

  return (
    sidebarData.map((item, index) => (
        <div
            typeof="button"
            key={index}
            href={item.pathname}
            className={`${styles.linkItem} ${
                    pathname === item.pathname ? styles.activeLink : ''
                }`}
            onClick={() => handleClose(item.pathname)}
        >
            {item.title}
        </div>
    ))
  )
}
