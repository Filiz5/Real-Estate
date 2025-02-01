'use client';

import PropertyCard from '@/components/cards/PropertyCard';
import { swAlert } from '@/helpers/swal';
import { deleteTourRequest } from '@/services/admin-tour-request';
import styles from '@/styles/components/admin/admin-tour-request/admin-tour-request-details.module.scss';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import Swal from 'sweetalert2';

export default function DetailsTourRequests({ data }) {
  const router = useRouter();

  const handleBack = () => {
    router.back(); // Önceki sayfaya dön
  };

  const handleDelete = async (id) => {
    try {
      const result = await Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
      });

     
      if (!result.isConfirmed) {
        return;
      }
      const res = await deleteTourRequest(id);
      if (res.success) {
        swAlert(res.message, 'success');
        router.back();
      } else {
        swAlert(res.message, 'error');
      }
    } catch (error) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: error.message
      });
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.containerCard}>
        <div className={styles.imageContainer}>
          <div className={styles.advert}>
            <Image
              src={
                data.advert.images.find((image) => image.featured === true)
                  ?.data || '/assets/images/image-not-found.jpg'
              }
              alt="Main Property"
              className={styles.advert_image}
              width={350}
              height={300}
              quality={300}
              priority
            />
          </div>

          <span className={styles.status}>
            {data.tourRequestStatus || 'Pending'}
          </span>
        </div>

        <div className={styles.details}>
          <div className={styles.header}>
            <h3 className={styles.title}>{data.advert.title}</h3>
          </div>
          <div className={styles.header}>
            <h3 className={styles.location}>
              {`${data.advert.district.name} ${data.advert.city.name} ${data.advert.country.name}`}
            </h3>
          </div>

          <div className={styles.row}>
            <div className={styles.field}>
              <div className={styles.label}>Tour Date</div>
              <input
                type="text"
                value={new Date(data.tourDate).toLocaleDateString() || ''}
                className={styles.input}
                disabled
              />
            </div>
            <div className={styles.field}>
              <div className={styles.label}>Tour Time</div>
              <input
                type="text"
                value={
                  new Date(data.tourDate).toLocaleTimeString([], {
                    hour: '2-digit',
                    minute: '2-digit'
                  }) || ''
                }
                className={styles.input}
                disabled
              />
            </div>
          </div>

          <div className={styles.priceContainer}>
            <p className={styles.price}>$ {data.advert.price.toFixed(2)}</p>
          </div>
          <div className={styles.row}>
            <button
              className={styles.button}
              type="button"
              onClick={handleBack}
            >
              Back
            </button>
            <button
              className={styles.button}
              onClick={() => handleDelete(data.id)}
            >
              Delete
            </button>
          </div>
        </div>
      </div>

      {/* Card Layout for Smaller Screens */}

      <div className={styles.card} key={data.id}>
        <div className={styles.cardImage}>
          <Image
            src={
              data.advert.images.find((image) => image.featured === true)
                ?.data || '/assets/images/image-not-found.jpg'
            }
            alt="Main Property"
            width={150}
            height={100}
          />
        </div>
        <div className={styles.cardHeader}>
          {data.advert?.title || 'No Title'}
        </div>
        <div className={styles.cardLocation}>
          {data?.advert?.district?.name}, {data?.advert?.city?.name},{' '}
          {data?.advert?.country?.name}
        </div>
        <div className={styles.cardLocation}>
          $ {data?.advert?.price.toFixed(2)}
        </div>
        <div className={styles.cardBody}>
          <div className={styles.cardRow}>
            <span className={styles.cardLabel}>Status:</span>
            <span className={styles.cardStatus}>{data.tourRequestStatus}</span>
          </div>
          <div className={styles.cardRow}>
            <span className={styles.cardLabel}>Tour Date:</span>
            <span className={styles.cardLabel}>
              {new Date(data.tourDate).toLocaleDateString()}
            </span>
          </div>
          <div className={styles.cardRow}>
            <span className={styles.cardLabel}>Tour Time:</span>
            <span className={styles.cardLabel}>
              {new Date(data.tourDate).toLocaleTimeString([], {
                hour: '2-digit',
                minute: '2-digit'
              })}
            </span>
          </div>
          <div className={styles.cardRow}>
            <button
              className={styles.button}
              type="button"
              onClick={handleBack}
            >
              Back
            </button>
            <button
              className={styles.button}
              onClick={() => handleDelete(data.id)}
            >
              Delete
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
