"use client";

import styles from '@/styles/errors/error-page.module.scss';
import { useRouter } from 'next/navigation';

export default function ErrorPage({ message }) {
    const router = useRouter();

    const toHome = () => {
        router.push('/');
    }
  return (
      <div className={styles.container}>
          <div className={styles.error_message}>
              {message}
          </div>
          <div className={styles.buttons}>
              <button className={styles.home}
              onClick={() => {toHome()}}
              >
               Ana Sayfaya Dön   
              </button>
              <button className={styles.refresh}
              onClick={() => {router.refresh()}}
              >
               Sayfayı Yenile   
              </button>
          </div>
          
    </div>
  )
}
