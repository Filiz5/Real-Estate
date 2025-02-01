import styles from "@/styles/components/common/no-data-available/no-data-available.module.scss"

const NoDataAvailable = ({text = "No data is available..."}) => {
  return (
    <div className={styles.container}>
        <div className={styles.innerContainer}>
            <p className={styles.text}>{text}</p>
        </div>
    </div>
  )
}

export default NoDataAvailable;
