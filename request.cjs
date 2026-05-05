const { exec } = require('node:child_process');

const total = 1000;
let count = total;
let completed = 0;
const start = performance.now();

while (count > 0) {
  exec(`curl -v -X POST http://localhost:4455 -H "Content-Type: application/json" -H "Authorization: Bearer mytoken123" -d '{"user": "test_dev", "action": "raw_server_test"}'
`, (err, stdout) => {
    if (err) console.error(err);
    else console.log(stdout);
    completed++;
    if (completed === total) {
      const elapsed = performance.now() - start;
      console.log(`\nAll ${total} requests completed in ${elapsed.toFixed(2)} ms`);
    }
  })
  --count;
}
