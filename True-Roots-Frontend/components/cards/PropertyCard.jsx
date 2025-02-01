import { useEffect, useState } from 'react';
import styles from '@/styles/components/cards/property-card.module.scss';
import { getImagesByImageIdList } from '@/services/getImagesByImageIdList-service';
import { convertBase64ToImages } from '@/helpers/functions/convert-data-to-image';
import Image from 'next/image';

const PropertyCard = ({ propertyData }) => {
  const [image, setImage] = useState('/assets/images/image-not-found.jpg'); // Default image
  const [hasFetched, setHasFetched] = useState(false); // Prevent multiple fetches

  useEffect(() => {
    const fetchImages = async () => {
      if (!propertyData?.images?.length || hasFetched) {
        return; // No images to fetch or already fetched
      }

      try {
        const imageIds = propertyData.images
          .filter((image) => image.featured)
          .map((image) => image.id);

        if (imageIds.length) {
          const advertImages = await getImagesByImageIdList({
            imageIdList: imageIds,
          });
          const fetchedImage = convertBase64ToImages(
            advertImages?.object[0]?.data
          );

          // Only update image if it has changed
          if (fetchedImage && fetchedImage !== image) {
            setImage(fetchedImage);
          }
        }
      } catch {
        setImage('/assets/images/image-not-found.jpg'); // Fallback image
      } finally {
        setHasFetched(true); // Mark fetch as complete
      }
    };

    fetchImages();
  }, [propertyData?.images, hasFetched, image]); // Only depends on `propertyData?.images`, `hasFetched`, and `image`

  return (
    <div className={styles.container}>
      <div className={styles.imageContainer} key={propertyData?.id}>
        <Image
          src={image}
          alt={propertyData?.title || 'Property Image'}
          width={315}
          height={279}
          className={styles.image}
        />

      </div>
    </div>
  );
};

export default PropertyCard;
