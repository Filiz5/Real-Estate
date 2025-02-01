'use client';
import Image from 'next/image';
import styles from '@/styles/components/dashboards/user-tour-request/tourRequestDetail.module.scss';
import { useState } from 'react';
import Swal from 'sweetalert2';
import { swAlert } from '@/helpers/swal';
import {formatDateTime} from '@/helpers/functions/formatDateTime';
import { updateTourRequest } from '@/actions/update-tourRequestForUser-action';
import { cancelTourRequest } from '@/actions/cancel-tourRequest-actions';

export const TourRequestDetail = ({res}) => {
  const advertId = res?.id;

  console.log('res', res);

    const [formDate, setFormDate] = useState({
      tourDate: '',
      tourTime: '',
      advertId
    });

    const timeFix = (time) => {
      const [hour, minute] = time.split(':');
      return `${hour}:${minute}`;
    };

    const [message, setMessage] = useState();

       const handleInputChange = (e) => {
         const { name, value } = e.target;

         setFormDate((prev) => ({
           ...prev,
           [name]: value
         }));
       };
    
    const handleUpdate = async (e) => {
    e.preventDefault();
    

    const time = formDate.tourTime
      ? formDate.tourTime
      : (res?.tourTime).slice(0, -3);
    const date = formDate.tourDate
      ? formDate.tourDate
      : res?.tourDate;
    const combinedDateTime = date;
            
     const updatedData = {
       tourDate: combinedDateTime, // Full date and time for tourDate
       tourTime: time, // Full date and time for tourTime
       advertId: formDate.advertId
     };

     console.log('updatedData', updatedData);

     try {
       const response = await updateTourRequest(res?.id, updatedData);
       if (response.success) {
         swAlert( response.message,'success','',4000);
       } else {
        swAlert(response.message, 'error', '', 4000);
       }
     } catch (error) {
       swAlert(response.message, 'error', '', 4000);
     }


    };

     const handleCancel = async (id) => {
        try {
          const result = await Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, cancel it!'
          });
    
          if (result.isConfirmed) {
            const res = await cancelTourRequest(id);
            if(!res.success){
              swAlert(res.message, 'error', '', 4000);
            }else{
            Swal.fire({
              title: 'Canceled!',
              text: 'Tour request has been canceled.',
              icon: 'success'
            }).then(() => {
              window.location.reload();
            });
          }
        }
        } catch (error) {
          swAlert(error.message, 'error', '', 4000);
        }
      };

  return (
    <div className={styles.container}>
      <div className={styles.advertImage}>
        <Image
          src={
            res?.advert?.images.find((image) => image.featured === true)
              ?.data || '/assets/images/image-not-found.jpg'
          }
          alt="Main Property"
          className={styles.image}
          width={350}
          height={150}
          quality={100}
          priority
        />
        <div className={styles.tourRequestStatus}>{res?.tourRequestStatus}</div>
      </div>
      <div className={styles.inputBody}>
        <div className={styles.bodyHeader}>
          <div className={styles.headerLeft}>
            <div className={styles.title}>{res?.advert?.title}</div>
            <div className={styles.location}>
              {' '}
              {res?.advert?.district?.name}, {res?.advert?.city?.name},{' '}
              {res?.advert?.country?.name}
            </div>
          </div>
          <div className={styles.price}>${res?.advert?.price}</div>
        </div>
        <div className={styles.inputUpdate}>
          <div className={styles.inputs}>
            <div className={styles.inputWrapper}>
              <label className={styles.label} htmlFor="">
                Tour Date
              </label>
              <input
                name="tourDate"
                type={formDate.tourDate ? 'date' : 'text'}
                value={formDate.tourDate || new Date(res.tourDate).toLocaleDateString()}
                onFocus={(e) => (e.target.type = 'date')}
                onChange={handleInputChange}
                className={styles.inputField}
              />
            </div>

            <div className={styles.inputWrapper}>
              <label className={styles.label} htmlFor="">
                Tour time
              </label>
              <input
                name="tourTime"
                type={formDate.tourTime ? 'time' : 'text'}
                defaultValue={formDate.tourTime || timeFix(res.tourTime)}
                onFocus={(e) => (e.target.type = 'time')}
                onChange={handleInputChange}
                className={styles.inputField}
              />
            </div>
          </div>
          <div className={styles.buttons}>
            <button className={styles.updateButton} onClick={handleUpdate}>
              Update
            </button>
            <button
              className={styles.cancelButton}
              onClick={() => handleCancel(res?.id)}
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

