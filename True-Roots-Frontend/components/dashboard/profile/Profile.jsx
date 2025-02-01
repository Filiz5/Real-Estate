"use client";

import styles from '@/styles/components/dashboards/profile/profile.module.scss';
import { useEffect } from 'react';
import UpdateUser from './UpdateUser';
import DeleteAccount from './DeleteAccount';


export default function Profile({ user }) {

  
 
  useEffect(() => {
  }, [user]);


  return (
    <main className={styles.container}>
      <UpdateUser user={user} />
      <DeleteAccount user={user} />  
    </main>
  );
}