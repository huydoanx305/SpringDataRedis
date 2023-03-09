# Setup

1. Download and install Redis server:
   <a href="/microsoftarchive/redis/releases/download/win-3.0.504/Redis-x64-3.0.504.msi" rel="nofollow">
   <span class="px-1 text-bold">Redis-x64-3.0.504.msi</span>
   </a>
2. Spring version 2.6.3 and add dependence 
   <br>
   <br>

<pre>&lt;<span class="pl-ent">dependency</span>&gt;
  &lt;<span class="pl-ent">groupId</span>&gt;org.springframework.data&lt;/<span class="pl-ent">groupId</span>&gt;
  &lt;<span class="pl-ent">artifactId</span>&gt;spring-data-redis&lt;/<span class="pl-ent">artifactId</span>&gt;
  &lt;<span class="pl-ent">version</span>&gt;2.6.3&lt;/<span class="pl-ent">version</span>&gt;
&lt;/<span class="pl-ent">dependency</span>&gt;
</pre>

<pre>&lt;<span class="pl-ent">dependency</span>&gt;
  &lt;<span class="pl-ent">groupId</span>&gt;redis.clients&lt;/<span class="pl-ent">groupId</span>&gt;
  &lt;<span class="pl-ent">artifactId</span>&gt;jedis&lt;/<span class="pl-ent">artifactId</span>&gt;
  &lt;<span class="pl-ent">version</span>&gt;3.7.1&lt;/<span class="pl-ent">version</span>&gt;
&lt;/<span class="pl-ent">dependency</span>&gt;
</pre>

3. Config trong file application.properties
4. Config in RedisConfig file

