"use client";

import { uploadImageAction } from '@/actions/advert-action';
import { initialResponse } from '@/helpers/form-validation';
import { swAlert } from '@/helpers/swal';
import styles from '@/styles/components/dashboards/update-advert/advert-fields/upload-image-fields.module.scss';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import { FaCheck } from 'react-icons/fa';

export default function ImageFields( {advertId} ) {
  const router = useRouter();
  const [images, setImages] = useState([]);
  const [selectedImageIndex, setSelectedImageIndex] = useState(null);

  useEffect(() => {
  }, [images]);

  const handleImageUpload = (event) => {
    const files = Array.from(event.target.files); // Support multiple files
    files.forEach((file) => {
      const reader = new FileReader();
      reader.onload = () => {
        setImages((prevImages) => [
          ...prevImages,
          { data: reader.result, featured: false, name: file.name, type: file.type }
        ]);
      };
      reader.readAsDataURL(file);
    });
  };

  const handleSelectImage = (index) => {
    setSelectedImageIndex(index);
  };

  const deleteImage = (index) => {
    setImages((prevImages) => prevImages.filter((_, i) => i !== index));
    if (selectedImageIndex === index) {
      setSelectedImageIndex(null); // Clear selection if deleted
    }
  };

  const handleUpload = async () => {
    if (images.length > 0) {
      const payload = {
        images: images
      };
      const res = await uploadImageAction(payload, advertId);
      if (res.success) {
        swAlert(res.message, 'success');
        setImages([]);
        router.refresh();
      } else if (res.message) {
        swAlert(res.message, 'error');
      }
    }else{
      swAlert('Please select images to upload', 'error');
    }
  }

  return (
    <div className={styles.container}>
      <p className={styles.head_line}>Add New Images</p>
      <div className={styles.add_container}>
        <label htmlFor="images" className={styles.add_button}>
          +
        </label>
        <input
          type="file"
          accept="image/*"
          onChange={handleImageUpload}
          id="images"
          name="images"
          multiple
        />
      </div>
      <div className={styles.image_container}>
        {images.map((image, index) => (
          <div
            key={index}
            className={`${styles.image_card} ${
              index === selectedImageIndex ? styles.selected : ''
            } ${image.featured ? styles.featured : ''}`}
            onClick={() => handleSelectImage(index)}
          >
            <div className={styles.image_overlay}>
              {image.featured ? (
                <span className={styles.checkmark}>
                  <FaCheck />
                </span>
              ) : (
                <span className={styles.circle}></span>
              )}
            </div>
            <Image
              src={image.data}
              alt={`Uploaded ${index}`}
              width={157}
              height={100}
            />
            {/* {image.featured && (
            <div className={styles.featured_label}>Featured</div>
          )} */}
          </div>
        ))}
        {images.length>0 ? <div className={styles.button_group}>
          <button
            className={`${styles.action_button} ${styles.delete}`}
            onClick={() => deleteImage(selectedImageIndex)}
            disabled={selectedImageIndex === null}
          >
            Delete
          </button>
        </div> : null}
        
      </div>
      <div className={styles.upload_container}>
        <button htmlFor="images" className={styles.upload_button} onClick={handleUpload}>
          Upload
        </button>
      </div>
    </div>
  );
}
