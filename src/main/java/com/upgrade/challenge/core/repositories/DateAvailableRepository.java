package com.upgrade.challenge.core.repositories;

import java.util.Date;

public interface DateAvailableRepository {

    Boolean dateAvailable(Date from, Date to, String email);

}
