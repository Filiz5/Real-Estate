"use client";

import styles from '@/styles/components/admin/admin-adverts-edit/admin-advert-edit.module.scss';
import AdvertFields from './AdvertFields';
import UserFileds from './UserFileds';
import ImageFields from './ImageFields';
import TourRequests from './TourRequests';
import { useEffect, useState } from 'react';
import Loading from '@/app/(admin)/admin-dashboard/loading';
import { wait } from '@/utils/wait';

export default function AdminAdvertEdit({ advert, user }) {
  const [advertInfo, setAdvertInfo] = useState(null);
  const [properties, setProperties] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const waitmethod = async () => {
      await wait(2000);
      setLoading(false);
    };
    waitmethod();
    
  }, []);

  useEffect(() => {
    setAdvertInfo({
      view: advert.object.viewCount || 0,
      favorites : advert.object.favorites.length || 0,
      tourRequests : advert.object.tourRequests.length || 0,
      createdAt : advert.object.createdAt || 'N/A',
      updatedAt : advert.object.updatedAt || 'N/A',
      builtIn : advert?.object?.builtIn,
    });

    setProperties(
      advert?.object?.categoryPropertyValues || []
    );


  }, [advert]);
  return (
    // loading ? 
    // <Loading /> :
    <main className={styles.container}>
      <AdvertFields advert={advert.object} properties={properties} />
      <UserFileds user={user.object} advertInfo={advertInfo}/>
      <ImageFields images={advert?.object?.images || []} />
      <TourRequests tourRequests={advert?.object?.tourRequests} />

    </main>
  )
}
