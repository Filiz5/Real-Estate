import Image from 'next/image';
import styles from '@/styles/components/admin/admin-adverts/image-cell.module.scss';

export default function ImageCell({ images, title, country, city, price }) {
  return (
    <div className={styles.container}>
      <div className={styles.image_container}>
        <Image
          className={styles.image}
          src={
            images.data
          }
          alt="advert"
          width={150}
          height={100}
          title={
            images.name
          }
        />
      </div>
      <div className={styles.advert_info}>
        <div className={styles.advert_title}>{title}</div>
        <div className={styles.advert_address}>
          {`${country}, ${city}` ||
            'Address not available'}
        </div>
        <div className={styles.advert_price}>${price}</div>
      </div>
    </div>
  );
}
