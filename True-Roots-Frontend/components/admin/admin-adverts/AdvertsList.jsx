import Image from 'next/image'
import { useEffect} from 'react';
import styles from '@/styles/components/admin/admin-adverts/adverts-list.module.scss';
import ImageCell from './ImageCell';
import { IoEye } from 'react-icons/io5';
import { MdFavorite } from 'react-icons/md';
import { IoLocationOutline } from 'react-icons/io5';
import EditAndDelete from './EditAndDelete';

export default function AdvertsList({adverts}) {
    useEffect(() => {
        
        }, [adverts]);
  return (
    <div className={styles.container}>
      <table className={styles.table}>
        <thead className={styles.thead}>
          <tr className={styles.head_row}>
            <th>
              <div className={styles.property_head}>
                <h5>Property</h5>
              </div>
            </th>
            <th>
              <div className={styles.date_head}>
                <h5>Date Published</h5>
              </div>
            </th>
            <th>
              <div className={styles.status_head}>
                <h5>Status</h5>
              </div>
            </th>
            <th>
              <div className={styles.views_head}>
                <h5>Views</h5>
              </div>
            </th>
            <th>
              <div className={styles.actions_head}>
                <h5>Actions</h5>
              </div>
            </th>
          </tr>
        </thead>
        <tbody>
          {adverts.map((advert, index) => (
            <tr key={index} className={styles.body_row}>
              <td>
                <ImageCell
                  images={advert.image}
                  title={advert.title}
                  country={advert.country.name}
                  city={advert.city.name}
                  price={advert.price}
                />
              </td>
              <td>
                <div className={styles.date}>
                  {advert.createdAt
                    ? new Date(advert.createdAt).toLocaleDateString()
                    : 'Date not available'}
                </div>
              </td>
              <td>
                <div className={styles.status}>{advert.advertStatus}</div>
              </td>
              <td>
                <div className={styles.views_container}>
                  <div className={styles.view}>
                    <IoEye />
                    {`${advert.viewCount}`}
                  </div>
                  <div className={styles.like}>
                    <MdFavorite />
                    {advert.favoriteCount}
                  </div>
                  <div className={styles.tour}>
                    <IoLocationOutline />
                    {advert.tourRequestCount}
                  </div>
                </div>
              </td>
              <td>
                <EditAndDelete advertId={advert.id} />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
