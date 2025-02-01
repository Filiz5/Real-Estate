'use client'; // Needed when using hooks in App Router

import { useRouter } from 'next/navigation'; // Use next/navigation instead of next/router
import { useState, useEffect } from 'react';
import { toggleFavorite} from '@/actions/toggleFavorite-action';
import { AiFillHeart, AiOutlineHeart } from 'react-icons/ai';
import styles from '@/styles/components/common/favorite-button.module.scss';

const FavoriteButton = ({ advertId, isFavorite: initialFavorite, isLoggedIn }) => {
  const router = useRouter();

  const [isFavorite, setIsFavorite] = useState(initialFavorite);

  // Ensure that the favorite state updates if initialFavorite prop changes
  useEffect(() => {
    setIsFavorite(initialFavorite);
  }, [initialFavorite]);

  const handleFavoriteClick = async () => {
    if (!isLoggedIn) {
      // Redirect to login page if the user is not logged in
      router.push('/login');
    } else {
      try {
        await toggleFavorite(advertId);
        setIsFavorite((prev) => !prev); // Toggle favorite state
      } catch (error) {
        console.error('Error toggling favorite: ', error.message);
      }
    }
  };

  return (
    <button onClick={handleFavoriteClick} className={styles.favoriteButton}>
      {isFavorite && isLoggedIn ? (
        <AiFillHeart className={styles.favoriteActive} />
      ) : (
        <AiOutlineHeart className={styles.favoriteInactive} />
      )}
    </button>
  );
};

export default FavoriteButton;
