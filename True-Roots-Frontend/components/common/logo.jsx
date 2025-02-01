

import React from 'react';
import Image from 'next/image';
import Link from 'next/link';
import { brand } from '@/constants/brand';
import styles from '@/styles/components/common/header/logo.module.scss';

export const Logo = ({filename}) => {
  return (
    <div className={styles.container}>
      <Link href="/" passHref>
        <Image
        className={styles.logo}
          src={`/assets/logo/${filename}.png`}
          width={180}
          height={80}
          alt={brand.name}
          priority
          
        />
      </Link>
    </div>
  );
};
