import styles from "@/styles/components/common/alert-text.module.scss"

export const AlertText = ({type, text}) => {
  return (
    <div className={`${styles.container} ${styles[type]}`}>
        <p className={styles.text}>{text}</p>
    </div>
  )
}
