'use client';
import styles from '@/styles/components/adverts/advertDetails.module.scss';
import { useRouter } from 'next/navigation';
import { IoLocationOutline } from 'react-icons/io5';
import { BiTagAlt } from 'react-icons/bi';
import { LuClock2 } from 'react-icons/lu';
import { MdOutlineRemoveRedEye } from 'react-icons/md';
import { getTimeElapsed } from '@/helpers/functions/timeElapsedCalculator';
import { tourRequestSchema } from '@/helpers/form-validation';
import { swAlert } from '@/helpers/swal';
import { useState } from 'react';
import { createTourRequest } from '@/actions/createTourRequest-action';
import ImageGallery from '@/components/common/ImageGallery';


export const AdvertDetails = ({ res, ownAdvert, session }) => {
  const advertId = res?.object?.id;
  const title = res?.object?.title;
  const city = res?.object?.city?.name;
  const district = res?.object?.district?.name;
  const advertTypeName = res?.object?.advertType.title;
  const createdAt = getTimeElapsed(res?.object?.createdAt);
  const viewCount = res?.object?.viewCount;
  const price = res?.object?.price;
  const images = res?.object?.images || [];
  const description = res?.object?.desc;
  const country = res?.object?.country.name;
  const categoryPropertyKeys =
    res?.object?.category?.categoryPropertyKeys || [];
  const categoryPropertyValues = res?.object?.categoryPropertyValues || [];

  const combinedCategoryProperty = categoryPropertyValues.map((valueItem) => {
    const keyItem = categoryPropertyKeys.find(
      (key) => key.id === valueItem.categoryPropertyKey.id
    );
    return {
      name: keyItem ? keyItem.name : 'Unknown',
      value: valueItem.value
    };
  });

  const [formDate, setFormDate] = useState({
    tourDate: '',
    tourTime: '',
    advertId
  });
 
  
   const router = useRouter();

  const handleRequest = async (e) => {
    e.preventDefault();

    if (!session) {
    
      router.push('/login');
    }else{
      try {
        // Combine tourDate and tourTime into a valid LocalDateTime format

        // Format the data to match the backend requirements
        const formattedData = {
          tourDate: formDate.tourDate, // LocalDateTime format
          tourTime: formDate.tourTime, // Time in HH:mm format
          advertId: formDate.advertId.toString()
        };
        
        await tourRequestSchema.validate(
          {
            date: formDate.tourDate,
            time: formDate.tourTime
          },
          { abortEarly: false }
        );

        console.log('formattedData', formattedData);

       
        const response = await createTourRequest(formattedData);

       if (response.success) {
         swAlert( response.message,'success','',4000);
         router.refresh();
       } else {
        swAlert(response.message, 'error', '', 4000);
       }
     } catch (error) {
       swAlert(response.message, 'error', '', 4000);
     }finally{
      setFormDate({ date: '', time: '' });
     }
    }
  };

  return (
    <>
      <div className={styles.AdvertDetailsHeader_container}>
        <div className={styles.left_header}>
          <div className={styles.title}>{title}</div>
          <div className={styles.subTitle}>
            <div>
              <IoLocationOutline /> {city}, {district}
            </div>
            <div>
              <BiTagAlt /> {advertTypeName}
            </div>
            <div>
              <LuClock2 /> {createdAt}
            </div>
            <div>
              <MdOutlineRemoveRedEye /> {viewCount}
            </div>
          </div>
        </div>
        <div className={styles.right_header}>${price}</div>
      </div>
      <div className={styles.body_container}>
        <div className={styles.details_container}>
          <div className={styles.img_card}>
            <ImageGallery images={images} />
          </div>
          <div className={styles.description_card}>
            <div className={styles.description}>
              <div className={styles.description_title}>Description</div>
              <div>{description}</div>
            </div>
          </div>
          <div className={styles.details_card}>
            <div className={styles.details}>
              <div className={styles.details_title}>Details</div>
              <ul className={styles.two_column_list}>
                {combinedCategoryProperty.map((item, index) => (
                  <li key={index}>
                    <div>{item.name}:</div>
                    <div> {item.value}</div>
                  </li>
                ))}
              </ul>
            </div>
          </div>
          <div className={styles.location_card}>
            <div className={styles.location}>
              <div className={styles.location_title}>Location</div>
              <div className={styles.address}>
                <div className={styles.country}>Country: {country}</div>
                <div className={styles.city}>City: {city}</div>
                <div className={styles.district}>District: {district}</div>
              </div>
              <div className={styles.map}>
                <iframe
                  src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d387191.0475295477!2d-74.31002915889563!3d40.69753799158023!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x89c24fa5d33f083b%3A0xc80b8f06e177fe62!2sNew%20York%2C%20NY!5e0!3m2!1sen!2sus!4v1734708808387!5m2!1sen!2sus"
                  width="100%"
                  height="350px"
                  style={{ border: '1px solid #3B3B3B', borderRadius: '20px' }}
                  loading="lazy"
                ></iframe>
              </div>
            </div>
          </div>
        </div>
        <div
          className={`${styles.tourRequest_container} ${
            ownAdvert ? styles.ownAdvert : ''
          }`}
        >
          <div className={styles.tourRequest_card}>
            <div className={styles.tourRequest_title}>Schedule a tour</div>
            <div className={styles.tourRequest_subTitle}>
              Choose your preferred day
            </div>
            <form className={styles.dateTimeRequest} onSubmit={handleRequest}>
              <div className={styles.inputWrapper}>
                <input
                  type={formDate.tourDate ? 'date' : 'text'}
                  value={formDate.tourDate}
                  onFocus={(e) => (e.target.type = 'date')}
                  onChange={(e) =>
                    setFormDate((prev) => ({
                      ...prev,
                      tourDate: e.target.value
                    }))
                  }
                  placeholder="Tour date"
                  className={styles.inputField}
                />
              </div>
              <div className={styles.inputWrapper}>
                <input
                  type={formDate.tourTime ? 'time' : 'text'}
                  value={formDate.tourTime}
                  onFocus={(e) => (e.target.type = 'time')}
                  onChange={(e) =>
                    setFormDate((prev) => ({
                      ...prev,
                      tourTime: e.target.value
                    }))
                  }
                  placeholder="Tour time"
                  className={styles.inputField}
                />

              </div>
              <button type="submit" className={styles.requestButton}>
                Submit a tour request
              </button>
            </form>
          </div>
        </div>
      </div>
    </>
  );
};
