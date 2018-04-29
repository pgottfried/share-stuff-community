package application.helper;

import java.util.function.Function;

import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.data.elasticsearch.core.facet.result.Term;

public class MyObjectMapper {

	public Function<Bucket, MyBucket> mapBucketToMyBucket = new Function<Bucket, MyBucket>() {
    
	    public MyBucket apply(Bucket t) {
	        MyBucket myBucket = new MyBucket();
	        myBucket.setKey(t.getKey());
	        myBucket.setCount(t.getDocCount());
	        return myBucket;
	    }
	};
	
	public Function<Term, MyBucket> mapTermToMyBucket = new Function<Term, MyBucket>() {
	    
	    public MyBucket apply(Term t) {
	        MyBucket myBucket = new MyBucket();
	        myBucket.setKey(t.getTerm());
	        myBucket.setCount(t.getCount());
	        return myBucket;
	    }

	};
	
	
}

