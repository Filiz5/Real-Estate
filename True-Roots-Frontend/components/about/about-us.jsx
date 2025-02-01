import Image from 'next/image';
import styles from '@/styles/components/about/about-us.module.scss';

export const AboutUs = () => {
  return (
    <div className={styles.container}>
      {/* Top Row */}
      <div className={styles.topRow}>
        <div className={styles.leftImage}>
          <Image
            className={styles.img}
            src="/assets/images/still-life-home-decor-cozy-home1.png"
            width={714}
            height={400}
            alt="life home decor cozy home"
          />
        </div>
        <div className={styles.rightText}>
          <p className={styles.title}>
            We&apos;re on a Mission to Change <br /> View of Real Estate Field.
          </p>
          <p>
            At the heart of our vision lies a resolute commitment to transform
            the landscape of the real estate industry. We&apos;re not just a
            company; we&apos;re on a mission to change the very essence of how
            real estate is perceived and experienced. Our journey is defined by
            innovation, transparency, and a relentless pursuit of excellence.
            With a bold and forward-thinking approach, we seek to revolutionize
            the traditional norms of the real estate field, making it more
            accessible, efficient, and customer-centric. Our aspiration is to
            shape a future where buying, selling, or investing in real estate is
            a seamless and empowering experience for all. Join us on this
            transformative journey as we rewrite the narrative of real estate.
          </p>
          <div className={styles.circles}>
            <div className={styles.circle}>
              <div></div>
              <p>Modern Architect</p>
            </div>
            <div className={styles.circle}>
              <div></div>
              <p>Green Building</p>
            </div>
          </div>
        </div>
      </div>

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
  );
};
