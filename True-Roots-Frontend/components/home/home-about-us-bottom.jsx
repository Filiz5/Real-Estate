
'use client'

import React from 'react'
import styles from '@/styles/components/about/about-us.module.scss';
import Image from 'next/image';

export const HomeAboutUsBottom = () => {
  return (
    <>
      <div className={styles.container}>
         {/* Bottom Row */}
      <div className={styles.bottomRow}>
        <div className={styles.leftText}>
          <div className={styles.leftTextBody}>
            <p className={styles.title}>
              Let’s Find The Right <br /> Selling Option For You
            </p>
            <div className={styles.features}>
              <div className={styles.feature}>
                <div className={styles.priceTag}>
                  <Image
                    src="/assets/icons/pricing_tags.png"
                    width={45}
                    height={45}
                    alt="Price Tag"
                  />
                </div>
                <div>
                  <p className={styles.subTitle}>Tech-Driven Marketing</p>
                  <p>
                    Real estate is embracing technology with virtual <br />
                    tours, 3D models, and blockchain transactions.
                  </p>
                </div>
              </div>
              <div className={styles.feature}>
                <div className={styles.air_quality}>
                  <Image
                    src="/assets/icons/Air_quality_control.png"
                    width={45}
                    height={45}
                    alt="Air quality control"
                  />
                </div>
                <div>
                  <p className={styles.subTitle}>Sustainability Matters</p>
                  <p>
                    Green building practices and eco-friendly features are
                    gaining popularity for environmentally conscious buyers.
                  </p>
                </div>
              </div>
              <div className={styles.feature}>
                <div className={styles.docvault}>
                  <Image
                    src="/assets/icons/docvault-svgrepo-com.png"
                    width={45}
                    height={45}
                    alt="Docvault"
                  />
                </div>
                <div>
                  <p className={styles.subTitle}>Remote Work Impact</p>
                  <p>
                    Changing work patterns are reshaping housing preferences,
                    favoring suburban and urban mixed-use developments.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className={styles.rightImage}>
          <Image
            className={styles.img}
            src="/assets/images/happy-couple-dancing-kitchen1.png"
            width={783}
            height={582}
            alt="happy couple dancing kitchen"
          />
        </div>
      </div>
      </div>
        
    </>
  )
}
