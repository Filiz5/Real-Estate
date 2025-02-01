import React from 'react'
import { GrFormNext, GrFormPrevious } from 'react-icons/gr';
import styles from '@/styles/components/home/ExploreProperties.module.scss';

export default function CityCards ({
  cityAdverts,
  currentIndex,
  cardsToShow,
  handleNext,
  handlePrev
}) {
  return (
      <div className={styles.outer_container}>
        <div className={styles.title}>
          <p>Explore Properties By Cities</p>
        </div>
        <div className={styles.inner_container}>
          <div className={styles.previous_button}>
            <button
              className={styles.button}
              onClick={handlePrev}
              disabled={currentIndex === 0}
            >
              <GrFormPrevious size={30} />
            </button>
          </div>
          <div className={styles.card_container}>
            {cityAdverts
              .slice(currentIndex, currentIndex + cardsToShow)
              .map((item, index) => (
                <div key={index} className={styles.card}>
                  <div className={styles.card_content}>
                    <p>{item.city}</p>
                    <p>
                      {item.amount} {item.amount > 1 ? 'Adverts' : 'Advert'}
                    </p>
                  </div>
                </div>
              ))}
          </div>
          <div className={styles.next_button}>
            <button
              className={styles.button}
              onClick={handleNext}
              disabled={currentIndex + cardsToShow >= cityAdverts.length}
            >
              <GrFormNext size={30} />
            </button>
          </div>
        </div>
      </div>
    );
}
