package com.bb.ragnar;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SeriesRepository extends CrudRepository<Series,Long>  {

	public List<Series> findByName(String name);
	public List<Series> findByDownloadDate(Date downloadDate);
}
