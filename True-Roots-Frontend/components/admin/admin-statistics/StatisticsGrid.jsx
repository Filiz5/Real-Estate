"use client";

import React from "react";
import styles from "@/styles/components/admin/admin-dashboard/admin-dashboard.module.scss";
import { FaUsers, FaLayerGroup, FaAd, FaBook, FaHandsHelping, FaPrint } from "react-icons/fa";
import Link from "next/link";

export default function StatisticsGrid({ statistics }) {
  if (!statistics) {
    return <p>Loading statistics...</p>;
  }

  return (
    <div className={styles.grid}>
      <Link href="/admin-dashboard/admin-users" className={styles.card}>
        <h2>Customers</h2>
        <p>{statistics.numberOfCustomer}</p>
        <div className={styles.icon}><FaUsers /></div>
      </Link>
      <Link href="/admin-dashboard/admin-categories" className={styles.card}>
        <h2>Categories</h2>
        <p>{statistics.numberOfCategory}</p>
        <div className={styles.icon}><FaLayerGroup /></div>
      </Link>
      <Link href="/admin-dashboard/admin-adverts" className={styles.card}>
        <h2>Adverts</h2>
        <p>{statistics.numberOfAdvert}</p>
        <div className={styles.icon}><FaPrint /></div>
      </Link>
      <Link href="/admin-dashboard/admin-advert-types" className={styles.card}>
        <h2>Advert Types</h2>
        <p>{statistics.numberOfAdvertType}</p>
        <div className={styles.icon}><FaBook /></div>
      </Link>
      <Link href="/admin-dashboard/admin-tour-requests" className={styles.card}>
        <h2>Tour Requests</h2>
        <p>{statistics.numberOfTourRequest}</p>
        <div className={styles.icon}><FaHandsHelping /></div>
      </Link>
    </div>
  );
}
