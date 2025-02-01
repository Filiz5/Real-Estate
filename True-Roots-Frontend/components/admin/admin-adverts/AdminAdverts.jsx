"use client";

import { useEffect, useState } from "react";
import AdvertsList from "./AdvertsList";
import SearchBox from "./SearchBox";
import styles from '@/styles/components/admin/admin-adverts/admin-adverts.module.scss';
import { Paginations } from "@/components/common/Paginations";
import NoDateFound from "@/components/common/NoDateFound";


export default function AdminAdverts({data}) {
  const {number, size, totalElements, totalPages} = data.object;
  const [adverts, setAdverts] = useState([]);
  const [currentUrl, setCurrentUrl] = useState('');

  useEffect(() => {
  }, []);

  useEffect(() => {
    const url = new URL(window.location.href);
    setCurrentUrl(url.pathname);
    setAdverts(data.object.content);
  }, [data]);
  return (
    <main className={styles.main_container}>
      <div className={styles.search_container}>
        <SearchBox />
      </div>
      <div className={styles.adverts_container}>
        <div className={styles.adverts_list}>
        {adverts.length === 0 ? 
          
          <NoDateFound text={'No Adverts found'} />
          :
          <AdvertsList  adverts={adverts}/>  
        }      
        </div>
        {adverts.length === 0 ? null :
        <div className={styles.adverts_pagination}>
          <Paginations 
            baseUrl={currentUrl}
            currentPage={number + 1}
            totalPages={totalPages}
            size={size}
          />

        </div>}

      </div>
    </main>
  );
}
