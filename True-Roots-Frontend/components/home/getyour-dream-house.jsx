"use client";

import Image from 'next/image';
import React, { useEffect, useState } from 'react';
import styles from '@/styles/components/home/GetYourDreamHouse.module.scss';
import { Button } from 'primereact/button';
import { useRouter } from 'next/navigation';

const GetYourDreamHouse = ({session}) => {
  const router = useRouter();
  const [token , setToken] = useState(null);
  useEffect(() => {
    if (session?.accessToken) {
      setToken(session.accessToken);
    }
  }, [session]);

  return (
    <div className={styles.dreamHouseSection}>
      <div className={styles.dreamHouseContent}>
        <h2>Get your dream house</h2>
        <p>
          Turn your aspirations into reality with &apos;Get Your Dream
          House&apos; – where your perfect home becomes a possibility.
        </p>
        {
          !token &&
          
          <Button
          label="Register Now"
          onClick={() => router.push('/login/register')}
          className={`p-button-rounded p-button-primary ${styles.login_button}`}
          icon="pi pi-arrow-up-right"
        />
        }
        
      </div>

      <div className={styles.dreamHouseImage}>
        <Image
          src="/assets/images/house.jpeg"
          alt="Dream House"
          width={400}
          height={300}
          priority
        />
      </div>
    </div>
  );
};

export default GetYourDreamHouse;
