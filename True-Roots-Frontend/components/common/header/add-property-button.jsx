import { useRouter } from 'next/navigation';
import Image from 'next/image';
import styles from '@/styles/components/common/header/add-property-button.module.scss';
import Link from 'next/link';

export const AddProperty = ({session}) => {
  const link = 'dashboard/add-new-advert';
  const loginLink = `/login?redirect=${link}`;
  const alt = 'add property';
  

  return (
    <Link
      href={session ? link : loginLink}
      title='Add Property'
      type='button'
      className={styles.addPropertyButtonLink}
    >
      <span className={styles.spanText}>Add Property</span>
      <Image
      className={styles.Image}
        src="/assets/images/login-right-arrow.png"
        width={40}
        height={40}
        alt={alt}
      />
    </Link>
  );
};
