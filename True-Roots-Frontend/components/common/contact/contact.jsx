import React from 'react';
import ContactBox from './contactbox';
import styles from '../../../styles/components/common/contact/contactbox.module.scss';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Spacer } from '../spacer/spacer';
import Image from 'next/image';

export const Contact = () => {
  return (
    <div className={styles.container}>
      <div className={styles.mapContainer}>
        <iframe
          src="https://www.google.com/maps/embed?pb=!1m18..."
          width="97%"
          height="600"
          style={{ border: '10px solid #fff', borderRadius: '20px' }}
          allowFullScreen
          loading="lazy"
          referrerPolicy="no-referrer-when-downgrade"
        ></iframe>
        <div className={styles.contactBoxContainer}>
          <ContactBox />
        </div>
      </div>

      <Spacer />

      <div className={styles.texttitle}>
        <h2 className={styles.texttitle1}>We’d Love To Hear From You.</h2>
        <p>
          We are here to answer any question you may have. As a partner of corporates, Realton has more than 9,000 offices of all sizes.
        </p>
      </div>

      <Spacer />

      <div style={{ textAlign: 'center' }}>
        <h2 style={{ color: 'black' }}>Visit Our Office</h2>
        <p style={{ color: 'black' }}>
          Realton has more than 9,000 offices of all sizes.
        </p>
        <div className="row">
          {officeData.map((office, index) => (
            <div key={index} className={`col-md-4 ${styles.officeItem}`}>
              <Image src={office.image} alt={office.city} width={93} height={93} />
              <p className={styles.officeLocation}>{office.city}</p>
              <p className={styles.officeAddress}>{office.address}</p>
              <p className={styles.officePhone}>{office.phone}</p>
            </div>
          ))}
        </div>
      </div>

      <Spacer />
    </div>
  );
};

const officeData = [
  {
    city: 'ANKARA',
    address: 'Mebusevleri, Akdeniz Cd. No:31, 06570 Çankaya/Ankara',
    phone: '0312 123 45 67',
    image: '/assets/images/contact/anitkabir-ankara.jpg'
  },
  {
    city: 'IZMIR',
    address: 'Cumhuriyet Bulvari No:1, 35250 Konak/İzmir',
    phone: '0232 123 45 67',
    image: '/assets/images/contact/saatkulesi-izmir.jpg'
  },
  {
    city: 'ISTANBUL',
    address: 'Bereketzade, 34421 Beyoğlu/İstanbul',
    phone: '0212 123 45 67',
    image: '/assets/images/contact/galatakulesi-istanbul.jpg'
  }
];
