import styles from '@/styles/components/privacyPolicy/privacy-policy.module.scss';
import { brand } from '@/constants/brand';
export const PrivacyPolicy = () => {
  return (
    <div className={styles.privacyPolicy}>
      <h1>Privacy Policy</h1>
      <p className="last-updated">Last updated: January 27, 2025</p>

      <section>
        <h2>Introduction</h2>
        <p>
          This Privacy Policy describes Our policies and procedures on the
          collection, use, and disclosure of Your information when You use the
          Service and tells You about Your privacy rights and how the law
          protects You.
        </p>
        <p>
          We use Your Personal Data to provide and improve the Service. By using
          the Service, You agree to the collection and use of information in
          accordance with this Privacy Policy.
        </p>
      </section>

      <section>
        <h2>Interpretation and Definitions</h2>
        <h3>Interpretation</h3>
        <p>
          Words with the initial letter capitalized have meanings defined under
          the following conditions. The definitions shall have the same meaning
          regardless of whether they appear in singular or plural.
        </p>
        <h3>Definitions</h3>
        <p>
          For the purposes of this Privacy Policy, the following definitions
          apply:
        </p>
        <ul>
          <li>
            <strong>Account:</strong> A unique account created for You to access
            our Service or parts of our Service.
          </li>
          <li>
            <strong>Affiliate:</strong> An entity that controls, is controlled
            by, or is under common control with a party.
          </li>
          <li>
            <strong>Cookies:</strong> Small files placed on Your device by a
            website, containing details of Your browsing history.
          </li>
          <li>
            <strong>Service:</strong> Refers to the Website.
          </li>
          <li>
            <strong>Usage Data:</strong> Data collected automatically, such as
            IP address and browser type.
          </li>
          <li>
            <strong>You:</strong> Refers to the individual accessing or using
            the Service, or a company/legal entity on whose behalf the
            individual is accessing the Service.
          </li>
        </ul>
      </section>

      <section>
        <h2>Collecting and Using Your Personal Data</h2>
        <h3>Types of Data Collected</h3>
        <h4>Personal Data</h4>
        <p>
          While using Our Service, We may ask You to provide Us with certain
          personally identifiable information, including but not limited to:
        </p>
        <ul>
          <li>Email address</li>
          <li>First name and last name</li>
          <li>Phone number</li>
        </ul>
        <h4>Usage Data</h4>
        <p>
          Usage Data is collected automatically when using the Service, such as
          IP address, browser type, and pages visited.
        </p>
      </section>

      <section>
        <h2>Tracking Technologies and Cookies</h2>
        <p>
          We use Cookies and similar technologies to improve and analyze Our
          Service. You can instruct Your browser to refuse Cookies, but some
          features of the Service may not function properly without them.
        </p>
      </section>

      <section>
        <h2>Use of Your Personal Data</h2>
        <p>
          We may use Your Personal Data to provide and maintain our Service,
          manage Your account, contact You, and for other purposes such as data
          analysis and marketing.
        </p>
      </section>

      <section>
        <h2>Children&apos;s Privacy</h2>
        <p>
          Our Service does not address anyone under the age of 13. If We become
          aware that We have collected Personal Data from a child under 13
          without parental consent, We will take steps to remove that
          information.
        </p>
      </section>

      <section>
        <h2>Changes to This Privacy Policy</h2>
        <p>
          We may update this Privacy Policy from time to time. Changes are
          effective when posted on this page. Please review it periodically for
          updates.
        </p>
      </section>

      <section>
        <h2>Contact Us</h2>
        <p>
          If you have any questions about this Privacy Policy, you can contact
          us by visiting our website at{' '}
          <a href={`${brand.website}/contact`}>{brand.website}/contact</a>
        </p>
      </section>
    </div>
  );
};
