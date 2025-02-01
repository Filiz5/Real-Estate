"use client";

import styles from '@/styles/components/common/no-data-found.module.scss';

export default function NoDateFound({text}) {
  return (
    <div className={styles.no_data}>{text}</div>
  )
}
