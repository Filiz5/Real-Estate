import { useEffect, useState } from 'react';
import styles from '@/styles/components/cards/advert-card.module.scss';
import { getImagesByImageIdList } from '@/services/getImagesByImageIdList-service';
import { convertBase64ToImages } from '@/helpers/functions/convert-data-to-image';
import Link from 'next/link';
import FavoriteButton from '@/components/common/FavoriteButton';
import Image from 'next/image';
import { useRouter } from 'next/navigation';

const AdvertCard = ({ id, advertData, session }) => {
  const [image, setImage] = useState(null);
  const isLoggedIn = session;
  const currentUserId = session?.user?.object?.userId;
  const router = useRouter();

  useEffect(() => {
    const fetchImages = async () => {
      try {
        const imageIds = advertData?.images
          .filter((image) => image.featured)
          .map((image) => image.id);

        if (imageIds?.length) {
          const advertImages = await getImagesByImageIdList({
            imageIdList: imageIds
          });
          const fetchedImage = convertBase64ToImages(
            advertImages?.object[0]?.data
          );
          setImage(fetchedImage);
        }
      } catch {
        setImage('/assets/images/image-not-found.jpg');
      }
    };

    fetchImages();
  }, [advertData]);

  const handlePush = (id) => {
    router.push(`/properties/${id}`);
  };

  return (
    <div className={styles.container}
      typeof='button' 
      onClick={() => {
        handlePush(advertData?.slug);
      }}
    >
      <div className={styles.imageContainer} key={id}>
        <Image
          src={image || '/assets/images/image-not-found.jpg'}
          alt="Advert Image"
          width={110}
          height={190}
          className={styles.image}
        />
        <div className={styles.favoriteButtonContainer}>
          <FavoriteButton
            advertId={id}
            isFavorite={advertData?.favorites.some(
              (fav) => fav.userId === currentUserId
            )}
            isLoggedIn={isLoggedIn}
            onClick={(e) => e.stopPropagation()} // Stop event propagation
          />
        </div>
        <div
          href={`/properties/${advertData?.slug}`}
          className={styles.loginLink}
        >
          <div className={styles.overlayContainer}>
            <div className={styles.titleLocation}>
              <div className={styles.title}>{advertData?.title}</div>
              <div className={styles.location}>
                {advertData?.district?.name}, {advertData?.city?.name}
              </div>
            </div>
            <div className={styles.price}>$ {advertData?.price}</div>
          </div>
        </div>
      </div>
    </div>
  );
};
export default AdvertCard;
