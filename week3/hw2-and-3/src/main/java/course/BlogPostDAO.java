/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package course;

import com.mongodb.*;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;

import java.util.Date;
import java.util.List;

public class BlogPostDAO {
    DBCollection postsCollection;

    public BlogPostDAO(final DB blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public DBObject findByPermalink(String permalink) {

        DBObject post = null;
        post = postsCollection.findOne(QueryBuilder.start("permalink").is(permalink).get());

        return post;
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<DBObject> findByDateDescending(int limit) {

        List<DBObject> posts = null;
        posts = postsCollection.find(new BasicDBObject()).limit(limit).toArray();

        return posts;
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();


        BasicDBObject post = new BasicDBObject();
        // XXX HW 3.2, Work Here
        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        /*
        {
            "_id" : ObjectId("513d396da0ee6e58987bae74"),
            "title" : "Martians to use MongoDB",
            "author" : "andrew",
            "body" : "Representatives from the planet Mars announced today that the planet would adopt MongoDB as a planetary standard. Head Martian Flipblip said that MongoDB was the perfect tool to store the diversity of life that exists on Mars.",
            "permalink" : "martians_to_use_mongodb",
            "tags" : [
                "martians",
                "seti",
                "nosql",
                "worlddomination"
            ],
            "comments" : [ ],
            "date" : ISODate("2013-03-11T01:54:53.692Z")
        }
        */

        // Build the post object and insert it
        post.append("title", title)
                .append("author",username)
                .append("body", body)
                .append("permalink",permalink)
                .append("tags",tags)
                .append("comments", new BasicDBList())
                .append("date", new Date());

        postsCollection.insert(post);

        return permalink;
    }




   // White space to protect the innocent








    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        // XXX HW 3.3, Work Here
        // Hints:
        // - email is optional and may come in NULL. Check for that.
        // - best solution uses an update command to the database and a suitable
        //   operator to append the comment on to any existing list of comments


        /*
        {
            "_id" : ObjectId("513d396da0ee6e58987bae74"),
            "author" : "andrew",
            "body" : "Representatives from the planet Mars announced today that the planet would adopt MongoDB as a planetary standard. Head Martian Flipblip said that MongoDB was the perfect tool to store the diversity of life that exists on Mars.",
            "comments" : [
                {
                    "author" : "Larry Ellison",
                    "body" : "While I am deeply disappointed that Mars won't be standardizing on a relational database, I understand their desire to adopt a more modern technology for the red planet.",
                    "email" : "larry@oracle.com"
                },
                {
                    "author" : "Salvatore Sanfilippo",
                    "body" : "This make no sense to me. Redis would have worked fine."
                }
            ],
            "date" : ISODate("2013-03-11T01:54:53.692Z"),
            "permalink" : "martians_to_use_mongodb",
            "tags" : [
                "martians",
                "seti",
                "nosql",
                "worlddomination"
            ],
            "title" : "Martians to use MongoDB"
        }
         */
        BasicDBObject comment = new BasicDBObject();
        comment.append("author",name);
        comment.append("body",body);
        if(email!=null) {
            comment.append("email",email);
        }

        postsCollection.update(QueryBuilder.start("permalink").is(permalink).get(),new BasicDBObject("$push",new BasicDBObject("comments", comment)));

    }


}
