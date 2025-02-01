import styles from '@/styles/components/admin/admin-adverts-edit/user-fields.module.scss';

export default function UserFileds({ user, advertInfo }) {

  return (
    <div className={styles.container}>
      <div className={styles.head}>
        <p className={styles.head_line}>User and Advert Info</p>
        <p className={styles.sub_head_line}></p>
      </div>
      <div className={styles.body}>
        <div className={styles.user}>
          <div className={styles.name}>
            <span>
              {user?.firstName} {user?.lastName}
            </span>
          </div>
          <div className={styles.email}>
            <span>{user?.email}</span>
          </div>
          <div className={styles.phone}>
            <span>{user?.phone}</span>
          </div>
        </div>
        <div className={styles.advertInfo_field1}>
          <div className={styles.item}>
            <span>View: </span>
            <span>{advertInfo?.view}</span>
          </div>
          <div className={styles.item}>
            <span>Favorites: </span>
            <span>{advertInfo?.favorites}</span>
          </div>
          <div className={styles.item}>
            <span>Tour requests: </span>
            <span>{advertInfo?.tourRequests}</span>
          </div>
        </div>
        <div className={styles.advertInfo_field2}>
          <div className={styles.item}>
            <span>Created at: </span>
            <span>{new Date(advertInfo?.createdAt).toLocaleDateString()}</span>
          </div>
          <div className={styles.item}>
            <span>Updated at: </span>
            <span>{new Date(advertInfo?.updatedAt).toLocaleDateString()}</span>
          </div>
          <div className={styles.item}>
            <span>Built in: </span>
            <span>{advertInfo?.builtIn}</span>
          </div>
        </div>
      </div>
    </div>
  );
}
